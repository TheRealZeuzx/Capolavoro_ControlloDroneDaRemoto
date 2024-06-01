package it.davincifascetti.controllosocketudp.videoTestingWorkspace;

import org.bytedeco.javacpp.*;
import org.bytedeco.ffmpeg.avcodec.*;
import org.bytedeco.ffmpeg.avutil.*;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.global.swscale;
import org.bytedeco.ffmpeg.swscale.SwsContext;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.util.Arrays;

public class VideoFrameDecoder {
    private AVCodecContext videoCodecContext = null;
    private AVPacket receivedVideoPacket = new AVPacket();
    private AVFrame decodedPicture = avutil.av_frame_alloc();
    private AVFrame processedPicture = avutil.av_frame_alloc();
    private BytePointer processPictureBuffer;

    @SuppressWarnings("deprecation")
    public IplImage decodeFromVideo(byte[] data, long timeStamp) {
        avcodec.av_init_packet(receivedVideoPacket); // Empty AVPacket

        byte frameFlag = data[1];
        byte[] subData = Arrays.copyOfRange(data, 5, data.length);
        BytePointer videoData = new BytePointer(subData);

        if (frameFlag == 0) { // If it is a key frame
            AVCodec codec = avcodec.avcodec_find_decoder(avcodec.AV_CODEC_ID_H264);
            if (codec != null) {
                videoCodecContext = avcodec.avcodec_alloc_context3(codec);
                videoCodecContext.width(320);
                videoCodecContext.height(240);
                videoCodecContext.pix_fmt(avutil.AV_PIX_FMT_YUV420P);
                videoCodecContext.codec_type(avutil.AVMEDIA_TYPE_VIDEO);
                videoCodecContext.extradata(videoData);
                videoCodecContext.extradata_size((int) videoData.capacity());
                videoCodecContext.flags2(videoCodecContext.flags2() | avcodec.AV_CODEC_FLAG2_CHUNKS);
                avcodec.avcodec_open2(videoCodecContext, codec, (AVDictionary) null);
                if ((videoCodecContext.time_base().num() > 1000) && (videoCodecContext.time_base().den() == 1)) {
                    videoCodecContext.time_base().den(1000);
                }
            } else {
                System.err.println("Codec could not be opened");
                return null;
            }
        }

        receivedVideoPacket.data(videoData);
        receivedVideoPacket.size((int) videoData.capacity());
        receivedVideoPacket.pts(timeStamp);
        videoCodecContext.pix_fmt(avutil.AV_PIX_FMT_YUV420P);

        int ret = avcodec.avcodec_send_packet(videoCodecContext, receivedVideoPacket);
        if (ret < 0) {
            System.err.println("Error sending a packet for decoding");
            return null;
        }

        ret = avcodec.avcodec_receive_frame(videoCodecContext, decodedPicture);
        if (ret < 0) {
            System.err.println("Error during decoding");
            return null;
        }

        int width = videoCodecContext.width();
        int height = videoCodecContext.height();
        int fmt = avutil.AV_PIX_FMT_BGR24;
        int size = avutil.av_image_get_buffer_size(fmt, width, height, 1);
        processPictureBuffer = new BytePointer(avutil.av_malloc(size));
        avutil.av_image_fill_arrays(processedPicture.data(), processedPicture.linesize(), processPictureBuffer, fmt, width, height, 1);

        // Convert the frame from YUV to BGR
        SwsContext swsCtx = swscale.sws_getContext(width, height, videoCodecContext.pix_fmt(),
                width, height, fmt, swscale.SWS_BILINEAR, null, null, (DoublePointer) null);
        swscale.sws_scale(swsCtx, decodedPicture.data(), decodedPicture.linesize(), 0, height, processedPicture.data(), processedPicture.linesize());

        IplImage returnImageFrame = IplImage.createHeader(width, height, opencv_core.IPL_DEPTH_8U, 3);
        returnImageFrame.imageData(new BytePointer(processedPicture.data(0)));

        return returnImageFrame;
    }

}