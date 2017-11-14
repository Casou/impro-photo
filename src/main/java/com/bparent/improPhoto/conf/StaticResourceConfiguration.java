package com.bparent.improPhoto.conf;

import com.bparent.improPhoto.util.IConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/handler/"+ IConstants.IPath.IPhoto.PHOTOS + "**").addResourceLocations("file:" + IConstants.IPath.IPhoto.PHOTOS);
        registry.addResourceHandler("/handler/playlist/**").addResourceLocations("file:" + IConstants.IPath.IAudio.AUDIOS_PLAYLIST);
        registry.addResourceHandler("/handler/jingles/**").addResourceLocations("file:" + IConstants.IPath.IAudio.AUDIOS_JINGLES);
        registry.addResourceHandler("/handler/videos/intro/**").addResourceLocations("file:" + IConstants.IPath.IVideo.VIDEO_INTRO);

        super.addResourceHandlers(registry);
    }
}
