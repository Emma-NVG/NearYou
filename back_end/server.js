let express         = require('express'),
    bodyParser      = require('body-parser'), //pour récupérer les résultats des post
    http            = require('http');
const { includeDefaultRoute } = require("./routes/DefaultRoute");


let app = express();
app.set('port', process.env.PORT || 3000);

app.use(bodyParser.urlencoded())
    .use(bodyParser.json());


// Redirect everything to HTTPS if it's the request use HTTP
/* app.use(function(req, res, next) {
    if (req.get('X-Forwarded-Proto') !== 'https') {
        res.redirect('https://' + req.get('Host') + req.url);
    } else {
        next();
    }
}); */


/* ======= Routes ======= */
includeDefaultRoute(app);


http.createServer(app).listen(app.get('port'), () => {
    console.log("API Listen ! Port : ", app.get('port'))
});
