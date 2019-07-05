var Msg = /** @class */ (function () {
    function Msg(ofType, a, b, c, d) {
        this.ofType = ofType;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    return Msg;
}());

var Tp = /** @class */ (function () {
    function Tp(tp1,tp2,tp3) {
        this.tp1 = tp1;
        this.tp2 = tp2;
        this.tp3 = tp3;
    }
    return Tp;
}());

var tousTps = new Tp("", "", "")   // que 3 tps 

// initialisation des balises pour tous les tps
for (var prop in tousTps){
    const res= $("#res")
    if (tousTps.hasOwnProperty(prop)){
        res.append('<il><b>'+prop+': </b> <label id='+prop+'>'+tousTps[prop]+'</label><br><br>')
    }
}


// Remplacement de la chaîne s dans la zone de résultat r
function resultString(r,s){
    const resObj= $("#"+r);
    resObj.text(s)
}

// Mise à jour des pour tous les tps
function update(res){
    for (var prop in res){
        resultString(prop,res[prop])
    }
}

$(document).ready(function () {
    $('#client-sends').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Pay", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        console.log('JSON='+JSON.stringify(p));
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
                console.log("success")
                console.log(data);
                var t = data;  // reads Tp results from the server
                update(t);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#merchant-sends').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Ack", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        console.log('JSON='+JSON.stringify(p));
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
                console.log("success")
                console.log(data);
                var t = data;  // reads Tp results from the server
                update(t);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#merchant-cancels').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Cancel", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), 0);
        console.log('JSON='+JSON.stringify(p));
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
                console.log("success")
                console.log(data);
                var t = data;  // reads Tp results from the server
                update(t);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
