class IntroVideosHandler {
    constructor() {
    }

    playVideo(videoName) {
        let video = $("div#div" + videoName + " video")[0];

        $("div.video video").each(function(index, vid) {
            vid.pause();
            $(vid).fadeOut(500);
        });
        $(video).delay(500).fadeIn(500);
        video.play();
    }

    pauseVideo(videoName) {
        $("div#div" + videoName + " video")[0].pause();
    }

    stopVideo(videoName) {
        let video = $("div#div" + videoName + " video")[0];
        video.pause();
        video.currentTime = 0;
    }

    onTimeUpdate(video) {

    }

}

