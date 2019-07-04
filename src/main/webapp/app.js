var Demo = /** @class */ (function () {
    function Demo(mes) {
        this.message = mes;
    }
    Demo.prototype.ClickGo = function () {
        $("#lblGo").text("You pressed the Go button");
    };
    return Demo;
}());
var Pay = /** @class */ (function () {
    function Pay(a, b, c, d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    return Pay;
}());
var dem = new Demo("bla bla bla bla");
$(document).ready(function () {
    $('#btnGo').click(function () {
        dem.ClickGo();
    });
    //$("#btnShowInfo").click(dem.ClickInfo); // doesn't work
    $('#client-sends').click(function () {
        console.log($("#client").val());
        // Je construit mon objet métier
        var p = new Pay(+$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        console.log(JSON.stringify(p));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/toserver',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(p),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                console.log(data);
                var t = data;
                $("#article1").append('<h2>Greetings ' + t.a + '</h2>');
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
//# sourceMappingURL=app.js.map