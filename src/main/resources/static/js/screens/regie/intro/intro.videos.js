class IntroVideosHandler {
    constructor(wsClient) {
        $("div.video video").each(function(index, video) {
            video.volume = 0;
        });
        this.wsClient = wsClient;
    }

    playVideo(videoName) {
        let spanButton = $("div#div" + videoName + " button.play span");
        let video = $("div#div" + videoName + " video")[0];

        if (spanButton.hasClass("fa-play")) {
            $("button.play span").addClass("fa-play").removeClass("fa-pause");
            $("div.video video").each(function(index, vid) {
                vid.pause();
            });
            spanButton.addClass("fa-pause").removeClass("fa-play");
            video.play();
            this.wsClient.sendMessage("/app/action/intro/playVideo", { message : videoName });
        } else {
            spanButton.addClass("fa-play").removeClass("fa-pause");
            video.pause();
            this.wsClient.sendMessage("/app/action/intro/pauseVideo", { message : videoName });
        }
    }

    stopVideo(videoName) {
        let spanButton = $("div#div" + videoName + " button.play span");
        let video = $("div#div" + videoName + " video")[0];
        video.pause();
        video.currentTime = 0;
        spanButton.addClass("fa-play").removeClass("fa-pause");
        this.wsClient.sendMessage("/app/action/intro/stopVideo", { message : videoName });
    }

    onTimeUpdate(video) {
        let percentage = (video.currentTime / video.duration) * 100;
        $(video).parents("div.video").first().find("div.videoControls div.progress").progressbar({
            value: percentage
        });
    }

}

