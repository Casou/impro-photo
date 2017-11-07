const ANIMATION_FADE_OUT = 4000;

class SalleAttenteScreen extends IScreen {
    constructor(nom, nextScreen) {
        super(nom, nextScreen);

        this.animation = new SalleAttenteAnimation();
    }

    init(status) {
        this.animation.initAndStart();
    }

    goToNextScreen(newStatus) {
        $('#salle_attente').fadeOut(5000, (function() {
            SCREENS[this.nextScreen].init(newStatus);
        }).bind(this));
    }

    subscriptions() {
        super.subscriptions();
        WEBSOCKET_CLIENT.subscribe("/topic/salle_attente/launchImpro", () => this.launchImpro());
    }


    launchImpro() {
        this.goToNextScreen();

        /*
        let returnObject = newReturnObject();
        let contentToInsert = null;

        $.ajax({
            type: 'POST',
            url: 'intro.php',
            dataType : 'html',
            async : false,
            success: function(data, textStatus, jqXHR) {
                // clearInterval(CLEAR_IMAGE_INTERVAL);
                // isAnimationActive = false;
                contentToInsert = data;

                returnObject.status = "OK";
                returnObject.message = "";
            },
            error: function(jqXHR, textStatus, errorThrown) {
                // alert("Une erreur est survenue : \n" + jqXHR.responseText);
                console.log(jqXHR);
                returnObject.status = "KO";
                returnObject.message = jqXHR.responseText;
            }
        });

        // $('section').addClass('animate_intro');
        $('section').fadeOut(ANIMATION_FADE_OUT);

        setTimeout(function() {
            stopAnimation();
            $('#content').html(contentToInsert);
            $('#currentScreen').val("INTRO");
        }, ANIMATION_FADE_OUT + 500);

        return returnObject;
        */
    }
}
