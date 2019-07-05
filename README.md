# ThomasDemo #

Ceci est une version simplifiée de ce qu'avait proposé Olivier en premier lieu.
Il ne reste plus que le serveur Scalatra <-JSON-> Client javascript.

Le workflow typique (pour modification et test local) est (sur les sources dans src/main):

1. mise à jour du serveur dans: scala/fr/istic/app
2. mise à jour de la page web dans webapp
   par ex. index-demo.html
3. mise à jour du code javascript (avec visual studio par ex) dans  webapp
   par ex. app-demo.html
3. compilation du merdier: dans thomasdemo lancer ```sbt``` puis dans sbt ```jetty:start```  pour démarrer le serveur.

5. ouvrir un navigateur sur l'URL 
   [http://localhost:8080/index-demo.html](http://localhost:8080/index-demo.html)

Dans index-demo et app-demo il y a un exemple proposé par Olivier au départ qui permet d'envoyer un objet depuis le client vers le serveur (un Pay(...)) et de récupérer un objet du même type envoyé par le serveur au client.

----------

Une seule fois après le git

```sh
$ cd thomasdemo
$ npm install
$ npm install -g typescript
```

```sh
$ sbt
> jetty:start
> browse
```

in other windows to compile typescript
```sh
$ tsc --watch
```


If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
# thomasdemo
