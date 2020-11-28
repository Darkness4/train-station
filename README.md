# Train Station

Par Marc NGUYEN et JB Rubio dans le cadre du projet Web + Android 2020.

## Objectifs et Spécifications

### API NestJS

- API REST

- Déployé sur CleverCloud

- GET /stations (Récupérer un résumé de toutes les données (i.e. seulement les infos les plus importantes pour l’affichage dans une liste + favori ou non))

  - Query Params : page : number

  - Exemple :

    - ```json
      {
          "data": [
              {
                  "datasetid":"liste-des-gares",
                  "recordid":"75f37016c5e5900a1f76039bb42d982cb90b93af",
                  "is_favorite":true,
                  "libelle":"Mulhouse-Dornach",
                  "record_timestamp":"2020-07-22T09:29:23.188000+00:00"
              },
              "..."
          ],
          "count": 10,
          "total": 10,
          "page": 10,
          "pageCount": 20,
      }
      ```

- (Bonus) POST /stations (Créer de nouvelles données)

  - Post-data : Station

- GET /stations/:recordid (pour l’affichage dans l’écran de détails)

  - Exemple :

    - ```json
      {
          "datasetid":"liste-des-gares",
          "recordid":"75f37016c5e5900a1f76039bb42d982cb90b93af",
          "is_favorite":true,
          "libelle":"Mulhouse-Dornach",
          "fields":{
              "commune":"MULHOUSE",
              "y_wgs84":47.74689819865143,
              "x_wgs84":7.308132635705053,
              "libelle":"Mulhouse-Dornach",
              "idgaia":"297f12aa-dfbc-11e3-a2ff-01a464e0362d",
              "voyageurs":"O",
              "geo_point_2d":[
                  47.7468981986,
                  7.30813263571
              ],
              "code_ligne":"115000",
              "x_l93":1022672.9941999987,
              "code_uic":"87182055",
              "c_geo":[
                  47.74689819865143,
                  7.308132635705053
              ],
              "rg_troncon":1,
              "geo_shape":{
                  "type":"Point",
                  "coordinates":[
                      7.308132635705054,
                      47.746898198623505
                  ]
              },
              "pk":"105+129",
              "idreseau":5712,
              "departemen":"HAUT-RHIN",
              "y_l93":6747305.211599998,
              "fret":"O"
          },
          "geometry":{
              "type":"Point",
              "coordinates":[
                  7.308132635705053,
                  47.74689819865143
              ]
          },
          "record_timestamp":"2020-07-22T09:29:23.188000+00:00"
      }
      ```

- PATCH /stations/:recordid (Mettre une donnée en favori ou non) (on n'utilisera pas PUT, car contraire aux [normes HTTP](https://tools.ietf.org/html/rfc5789))

  - Post-data (replace): Station(partiel)

- (Bonus) Au lieu de stocker des données dans un fichier JSON, faire une requête au démarrage de l’API pour récupérer les données

- 

## App Android

- Récupération des données de l’api puis affichage dans une liste et un écran avec le détail

- Possibilité de mettre en favori certains éléments

- Maquette :

  ![maquette](assets/image-20201128010714763.png)

- Application composée au minimum de : 

  - 2 Fragment (la liste + l’ecran avec les infos)
  - 2 Activity

- Une Toolbar sera présente et permettra de rafraîchir les données récupérées et affichées

- (Bonus) Mise en place d’un système de recherche/filtre sur la liste affichée

- (Bonus) Mise en place d’une base de données locale pour afficher la liste d’élément en mode hors connexion

- (Bonus) Utilisation de LiveData ou d’Observable pour la récupération de données dans la BDD

## Documentation

### API

### Compiler et lancer

```sh
npm run build
npm run start:prod
# Ou
npm run start:nest
```

### Déployer sur CleverCloud

Le déploiement est automatisé par Github Actions.

A chaque commit, l'API NestJS est testé puis déployé sur une branche Github `release/api`.

La branche `release/api` est synchronisé sur CleverCloud, et permet de déployer un Docker qui compile et lance l'API.

### Fonctionnement de l'API

#### Entités

Les entités sont pratiquement les même que sur le [JSON de la SNCF](https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json).

Les seules différences sont dans le fichier [station.ts](./train-station-api/src/entities/station.ts) où deux champs ont été rajouté :

- `is_favorite` : Si la station est le favoris de l'utilisateur.
- `libelle` : Qui correspond au champ `fields.libelle`. Nous l'avons copié pour l'affichage de la liste.

#### Repositories et Services

Nous utilisons TypeORM pour stocker les Stations dans l'API.

Nous utilisons ensuite le paquet **[nestjsx/crud](https://github.com/nestjsx/crud)** pour implémenter rapidement le CRUD notre service en héritant de `TypeOrmCrudService<Station>`.

Nous ajoutons notre propre méthode `getOneDetail` qui se charge de récupérer les `Station` joint avec les autres entités.

```typescript
  getOneDetail(req: CrudRequest): Promise<Station> {
    req.options.query.join = {
      fields: {
        eager: true,
      },
      geometry: {
        eager: true,
      },
      'fields.geo_shape': {
        eager: true,
      },
    };
    return this.getOne(req);
  }
```

Nous téléchargeons les données au démarrage de l'API avec :

```typescript
this.subscription = client
      .get<StationModel[]>('https://ressources.data.sncf.com/explore/dataset/liste-des-gares/download/?format=json')
      .pipe(mergeMap((response) =>
          Promise.all(response.data.map((e) => Station.fromModel(e).save())),
      ))
      .subscribe();
```

#### Controllers

Encore une fois, avec **[nestjsx/crud](https://github.com/nestjsx/crud)**, nous exposons uniquement les points d'entrées de notre API et activons la pagination.

L'API est paginé grâce aux paramètres de requête `limit` et `page`. Exemple : `GET "http://train-station.cleverapps.io/stations?limit=10&page=2"`

L'API possède également une barre de recherche avec le paramètre de requête `q`. Voir [documentation du paquet](https://github.com/nestjsx/crud/wiki/Requests#search).

Nous écrasons la méthode `getOne` par la notre afin d'utiliser la méthode de notre service `getOneDetail` au lieu de `getOneBase` du paquet.

```typescript
@Controller('stations')
export class TrainStationController implements CrudController<Station> {
  constructor(readonly service: TrainStationService) {}

  @Override()
  getOne(@ParsedRequest() req: CrudRequest): Promise<Station> {
    return this.service.getOneDetail(req);
  }
}
```

#### Pas nécessaire mais utile

L'API est basé sur Fastify.

L'API limite également le nombre de requêtes par minute.

L'API redirige `/` vers `/api`, où il y a un client Swagger pour tester l'API.

![image-20201128030150536](assets/image-20201128030150536.png)

