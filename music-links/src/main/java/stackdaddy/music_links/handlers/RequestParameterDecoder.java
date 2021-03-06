package stackdaddy.music_links.handlers;

import stackdaddy.music_links.models.SearchDetails;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ryanstack on 3/28/16.
 */
public class RequestParameterDecoder extends ChannelInboundHandlerAdapter {

    static Logger LOGGER = LoggerFactory.getLogger(RequestParameterDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        HttpRequest httpRequest = (HttpRequest) msg;

        QueryStringDecoder queryDecoder = new QueryStringDecoder(httpRequest.getUri());
        Map<String, List<String>> params = queryDecoder.parameters();
        String track  = StringUtils.join(params.get("track"), " ");
        String artist = StringUtils.join(params.get("artist"), " ");
        String album  = StringUtils.join(params.get("album"), " ");

        LOGGER.info("printing out request parameters");
        LOGGER.info(track);
        LOGGER.info(artist);
        LOGGER.info(album);

        //Release the HttpRequest Object
        ReferenceCountUtil.release(httpRequest);

        //Is there a better way so I don't have to remember order of input
        SearchDetails searchDetails = new SearchDetails(artist, album, track);
        ctx.fireChannelRead(searchDetails);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
