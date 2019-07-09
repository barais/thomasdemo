var Payd = /** @class */ (function () {
    function Payd(a, b, c, d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    return Payd;
}());

$(document).ready(function () {
    $('#client-sends').click(function () {
        console.log($("#client").val());
        // Je construit mon objet métier
        var p = new Payd(+$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        console.log('JSON='+JSON.stringify(p));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/todemo',    //l'url où la requête du client va être envoyée (on va trouver la même dans MyScalaServlet)... on peut en avoir plusieurs!
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(p),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                console.log("success!")
                console.log(data);
                var t = data;
                const resObj1= $("#res1");
                const resText1= resObj1.text();
                resObj1.text(resText1 + t.a);

                const resObj2= $("#res2");
                resObj2.append('<b>'+ t.a +"," + t.b + '</b>')
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
