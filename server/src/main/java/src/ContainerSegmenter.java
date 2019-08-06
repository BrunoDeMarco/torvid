/*******************************************************************************
 * Copyright (c) 2014, Art Clarke.  All rights reserved.
 *
 * This file is part of Humble-Video.
 *
 * Humble-Video is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Humble-Video is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Humble-Video.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package src;

import java.io.IOException;

import io.humble.video.BitStreamFilter;
import io.humble.video.Coder;
import io.humble.video.Decoder;
import io.humble.video.Demuxer;
import io.humble.video.DemuxerStream;
import io.humble.video.MediaDescriptor.Type;
import io.humble.video.MediaPacket;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;

/**
 * Takes a mp4, mov or other MP4 variant file, and segments it into smaller files that comply with the HTTP Live Streaming spec.
 *
 * This is meant as a demonstration program to teach the use of the Humble API.
 * <p>
 * Concepts introduced:
 * </p>
 * <ul>
 * <li>Re-muxing: The idea of changing containers while not actually re-encoding the data.</li>
 * <li>Bit Stream Filters: Some containers (like MPEG2TS) require slightly different packets (like annexb: http://www.szatmary.org/blog/25) layouts.
 *     Bit Stream Filters can re-write packets inline to different layouts.</li>
 * </ul>
 *
 * <p> 
 * To run from maven, do:
 * </p>
 * <pre>
 * mvn -DskipTests install exec:java -Dexec.mainClass="io.humble.video.demos.ContainerSegmenter" -Dexec.args="-vf h264_mp4toannexb inputfile.mp4 output.m3u8"
 * </pre>
 * @author aclarke
 *
 */
public class ContainerSegmenter {

    private static void segmentFile(String input, String output, int hls_start,
                                    int hls_time, int hls_list_size, int hls_wrap, String hls_base_url,
                                    String vFilter,
                                    String aFilter) throws InterruptedException, IOException {

        final Demuxer demuxer = Demuxer.make();

        demuxer.open(input, null, false, true, null, null);

        // we're forcing this to be HTTP Live Streaming for this demo.
        final Muxer muxer = Muxer.make(output, null, "hls");
        muxer.setProperty("start_number", hls_start);
        muxer.setProperty("hls_time", hls_time);
        muxer.setProperty("hls_list_size", hls_list_size);
        muxer.setProperty("hls_wrap", hls_wrap);
        if (hls_base_url != null && hls_base_url.length() > 0)
            muxer.setProperty("hls_base_url", hls_base_url);

        final MuxerFormat format = MuxerFormat.guessFormat("mp4", null, null);

        /**
         * Create bit stream filters if we are asked to.
         */
        final BitStreamFilter vf = vFilter != null ? BitStreamFilter.make(vFilter) : null;
        final BitStreamFilter af = aFilter != null ? BitStreamFilter.make(aFilter) : null;

        int n = demuxer.getNumStreams();
        final Decoder[] decoders = new Decoder[n];
        for(int i = 0; i < n; i++) {
            final DemuxerStream ds = demuxer.getStream(i);
            decoders[i] = ds.getDecoder();
            final Decoder d = decoders[i];

            if (d != null) {
                // neat; we can decode. Now let's see if this decoder can fit into the mp4 format.
                if (!format.getSupportedCodecs().contains(d.getCodecID())) {
                    throw new RuntimeException("Input filename (" + input + ") contains at least one stream with a codec not supported in the output format: " + d.toString());
                }
                if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
                    d.setFlag(Coder.Flag.FLAG_GLOBAL_HEADER, true);
                d.open(null, null);
                muxer.addNewStream(d);
            }
        }
        muxer.open(null, null);
        final MediaPacket packet = MediaPacket.make();
        while(demuxer.read(packet) >= 0) {
            /**
             * Now we have a packet, but we can only write packets that had decoders we knew what to do with.
             */
            final Decoder d = decoders[packet.getStreamIndex()];
            if (packet.isComplete() && d != null) {
                // check to see if we are using bit stream filters, and if so, filter the audio
                // or video.
                if (vf != null && d.getCodecType() == Type.MEDIA_VIDEO)
                    vf.filter(packet, null);
                else if (af != null && d.getCodecType() == Type.MEDIA_AUDIO)
                    af.filter(packet, null);
                muxer.write(packet, true);
            }
        }

        // It is good practice to close demuxers when you're done to free
        // up file handles. Humble will EVENTUALLY detect if nothing else
        // references this demuxer and close it then, but get in the habit
        // of cleaning up after yourself, and your future girlfriend/boyfriend
        // will appreciate it.
        muxer.close();
        demuxer.close();

    }


    /**
     */
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws InterruptedException, IOException {
        try {
            segmentFile("/home/bruno.appolonio/Projects/torvid/server/video-in/SampleVideo_1280x720_10mb.mkv", "test", 0, 2, 0, 0, "", null, null);
        } catch (Exception e) {
            System.err.println("Exception parsing command line: " + e.getLocalizedMessage());
        }
    }

}