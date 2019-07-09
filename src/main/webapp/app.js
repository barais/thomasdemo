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

var msgHistory= new Array();

// On remplace le toString des Msg de façon à avoir des mesages Pay/Ack/Cancel dans le navigateur
Msg.prototype.toString= function toString(){
    var s='';
    if (this.ofType!='Cancel') s=','+this.d;
    return(this.ofType+'('+this.a+','+this.b+','+this.c+s+')')
}

// var Tp = /** @class */ (function () {
//     function Tp(tp1,tp2,tp3) {
//         this.tp1 = tp1;
//         this.tp2 = tp2;
//         this.tp3 = tp3;
//     }
//     return Tp;
// }());

var Tpassoc = /** @class */ (function () {
    function Tpassoc(name,resultat) {
        this.name = name;
        this.resultat = resultat;
    }
    return Tpassoc;
}());

var tousTps = ["genetProved","Adili"];

// initialisation des balises pour tous les tps
for (var tp of tousTps){
    const res= $("#res")
    res.append('<il><b>'+tp+': </b> <label id='+tp+'>'+""+'</label><br><br>')
}

// Remplacement de la chaîne s dans la zone de résultat r
function resultString(r,s){
    const resObj= $("#"+r);
    resObj.text(s)
}

// Mise à jour de l'historique des messages et du résultat pour tous les tps
function update(res,h){
    // res= le retour du serveur
    // h= l'historique des messages
    resL=res.length;
    for (i=0; i<resL; i++){
        resultString(res[i].name,res[i].res)
    }
    const histo= $("#messages");
    histo.text(h);
}

// Les fonctions associées aux boutons du document
$(document).ready(function () {
    $('#client-sends').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Pay", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        msgHistory.push(p);
        console.log('JSON='+JSON.stringify(msgHistory));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/toserver',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(msgHistory),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                var ret = data;  // reads Tp results from the server
                console.log(data);
                update(ret,msgHistory);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#merchant-sends').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Ack", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), +$("#amount").val());
        msgHistory.push(p);
        console.log('JSON='+JSON.stringify(msgHistory));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/toserver',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(msgHistory),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                var ret = data;  // reads Tp results from the server
                console.log(data);
                update(ret,msgHistory);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $('#merchant-cancels').click(function () {
        // Je construis mon objet métier
        var p = new Msg("Cancel", +$("#client").val(), +$("#merchant").val(), +$("#transaction").val(), 0);
        msgHistory.push(p);
        console.log('JSON='+JSON.stringify(msgHistory));
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/toserver',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(msgHistory),
            dataType: 'json',
            success: function (data) {
                //On ajax success do this
                var ret = data;  // reads Tp results from the server
                console.log(data);
                update(ret,msgHistory);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
