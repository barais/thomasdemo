class Demo {
    message: string;

     constructor(mes: string) {
        this.message = mes;
    }

     ClickGo() {
        $("#lblGo").text("You pressed the Go button");
    }


}


class Pay{
    constructor (public a: number, public b: number,public  c: number,public  d:number){}
}


 let dem = new Demo("bla bla bla bla");

 $(document).ready(function () {
    $('#btnGo').click(function () {
        dem.ClickGo();
    });
    //$("#btnShowInfo").click(dem.ClickInfo); // doesn't work
    $('#client-sends').click(function () {

        console.log($("#client").val());
        // Je construit mon objet métier
        const p = new Pay(+$("#client").val(), 
        +$("#merchant").val(), 
        +$("#transaction").val(), 
        +$("#amount").val());

        console.log(JSON.stringify(p))
        // Je l'envoie au serveur
        console.log(jQuery);
        $.ajax({
            url: '/toserver',
            type: 'POST',
            contentType:'application/json',
            data: JSON.stringify(p),
            dataType:'json',
            success: function(data){
              //On ajax success do this
                  console.log(data);
               },
            error: function(error) {
                console.log(error);
             }
          });

    });
});