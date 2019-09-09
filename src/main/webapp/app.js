// Le format des messages vu depuis javascript 
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

// L'historique des messages
var msgHistory= new Array();

// On remplace le toString des Msg de façon à avoir des messages Pay/Ack/Cancel dans le navigateur
Msg.prototype.toString= function toString(){
    var s='';
    if (this.ofType!='Cancel') s=','+this.d;
    return(this.ofType+'('+this.a+','+this.b+','+this.c+s+')')
}

// On récupère la liste de tous les noms de Tps
// et on complète la page web avec les balises nécessaires
$.ajax({
	url: '/toserver',
	type: 'GET',
	datatype : 'json',
	success: function(data){
		initResPage(data);
	},
	error: function(error){
		console.log("init error:"+error);
	}

})

// initialisation des balises pour afficher les résultats de tous les tps
function initResPage(ltps){
	const res= $("#res");
	res.append('<ul>');
	for (var tp of ltps){
		res.append('<li><b>'+tp.name+': </b> <label id='+tp.name+'>'+""+'</label></li><br>');
	}
	res.append('</ul>')
}

// Remplacement de la chaîne s dans la zone de résultat r
function resultString(r,s){
    const resObj= $("#"+r);
    resObj.text(s)
}

// Mon toString pour les tableaux de messages
function mytostring(t){
	const temp= t.map(x => x.toString());
	return temp.join(', ')
}

// Mise à jour de l'historique des messages et du résultat pour tous les tps
// Tout ce que l'on sait sur ret (passé depuis scala vers javascript en passant par JSON)
// c'est que c'est un tableau d'objets dont 2 des champs sont
// name et resultat
function update(ret,h){
    // res= le retour du serveur
    // h= l'historique des messages
    const retL=ret.length;
    for (i=0; i<retL; i++){
        resultString(ret[i].name,(ret[i].resultat+ret[i].correct))
    }
    const histo= $("#messages");
    console.log("avant: "+h)
    histo.text(mytostring(h))
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
                update(ret,msgHistory);
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});
