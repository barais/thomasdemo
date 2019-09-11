// La borne sur les valeurs entières
function bound(a){
    if (isNaN(a) | a<0) return 0
    else if (a>10000) return 10000
          else return a
}

// Le format des messages vu depuis javascript 
var Msg = /** @class */ (function () {
    function Msg(ofType, a, b, c, d) {
        this.ofType = ofType;
        this.a = bound(a);
        this.b = bound(b);
        this.c = bound(c);
        this.d = bound(d);
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

//le verrou qui empêche document.ready de démarrer!
$.holdReady(true);
// On récupère la liste de tous les noms de Tps
// et on complète la page web avec les balises nécessaires
$.ajax({
	url: '/toserver',
	type: 'GET',
	datatype : 'json',
	success: function(data){
        initResPage(data);
        $.holdReady(false); //maintenant document.ready va pouvoir lancer le listener!
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
        res.append('<li>');
        // on ajoute les boutons pour les propriétés
        for (i=1;i<10;i++){
            res.append('<button type="button" class="prop" id='+tp.name+"_prop_"+i+' name='+tp.name+"_prop_"+i+' disabled=true>'+i+'</button>')
            //console.log('<button class="prop" id="'+tp.name+"_prop_"+i+'" name="'+tp.name+"_prop_"+i+'">'+i+'</button>')
            }
        // on ajoute le nom du TP
        res.append ('<b> '+tp.name+': </b>');
        // on ajoute le label qui sera complété par les transactions
        // validées ou non
        res.append('<label id='+tp.name+'>'+" List()"+'</label>')  //label dont le contenu est vide au départ
        res.append('</li>');
	}
    res.append('</ul>');
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
        resultString(ret[i].name,(ret[i].resultat))
        // On active/désactive les boutons pour les propriétés
        // en fonction de la valeur de ret[i].correct
        for (j=1; j<10; j++){
            $('#'+ret[i].name+'_prop_'+j).attr("disabled",ret[i].correct);
        }
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
        console.log(jQuery);
        // Je l'envoie au serveur
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

    // On assigne à chaque bouton "propriété" la bonne action!
    $( ".prop" ).each(function(index) {    // Pout tous les boutons ayant class=prop
        $(this).on("click", function(){
            const tempHisto= Array.from(msgHistory)
            // Dummy message with the prop name and tp name
            const p = new Msg($(this).attr('id'), 0, 0, 0, 0);
            tempHisto.unshift(p); // We add the message as first element of the temporary message sequence 
            console.log(msgHistory);
            console.log(tempHisto);
            console.log()
            var boolKey = $(this).attr('id');  // on logue l'attribut id du bouton!
            console.log(boolKey);     
            $.ajax({
                url: '/toserverProp',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(tempHisto),
                dataType: 'json',
                success: function (data) {
                },
                error: function (error) {
                    console.log(error);
                }
            });
        });
    });

    // for (var j=1; j<10; j++){
    //     $('#CHENAA_prop_'+j).click(function(){
    //         console.log('click '+j);
    //     });
    // };

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
