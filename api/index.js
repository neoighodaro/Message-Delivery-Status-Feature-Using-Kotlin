// Load packages
const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const Pusher = require('pusher');

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// Temp Variables
var userId = 0;
var messageId = 0;

// Pusher instance
var pusher = new Pusher({
    appId: 'PUSHER_APP_ID',
    key: 'PUSHER_APP_KEY',
    secret: 'PUSHER_APP_SECRET',
    cluster: 'PUSHER_APP_CLUSTER',
    encrypted: true
});

// POST: /message
app.post('/message', (req, res) => {
    messageId++;

    pusher.trigger('my-channel', 'new-message', {
        "id": messageId,
        "message": req.query.msg,
        "sender": req.query.sender,
    });

    res.json({ id: messageId, sender: req.query.sender, message: req.query.msg })
})

// POST: /delivered
app.post('/delivered', (req, res) => {
    pusher.trigger('my-channel', 'delivery-status', {
        "id": req.query.messageId,
        "sender": req.query.sender,
    });

    res.json({ success: 200 })
})

// POST: /auth
app.post('/auth', (req, res) => {
    userId++;
    res.json({ id: "userId" + userId })
})

// GET: /
app.get('/', (req, res, next) => res.json("Working!!!"))

// Serve application
app.listen(9000, _ => console.log('Running application...'))
