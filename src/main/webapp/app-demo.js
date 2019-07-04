var Pay = /** @class */ (function () {
    function Pay(a, b, c, d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    return Pay;
}());
$(document).ready(function () {
    $('#client-sends').click(function () {
        console.log($("#client").val());
        // Je construit mon objet métier
        var p = new Pay(+$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        console.log('JSON='+JSON.stringify(p));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/todemo',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(p),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                console.log(data);
                var t = data;
                const resObj= $("#res");
                const resText= resObj.text();
                resObj.text(resText + t.a);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
//# sourceMappingURL=app.js.map