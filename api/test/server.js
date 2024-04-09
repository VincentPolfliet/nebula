const express = require('express');

const db = require('./db.json')

const app = express();
const port = process.env.PORT || 1337;

app.get('/my/ships', function (req, res) {
    const params = req.query

    const ships = db['ships']

    const page = params.page || 1
    const limit = params.limit || 10

    const data = paginate(ships, page, limit)
    res.send({meta: {total: ships.length, page, limit}, data})
})

function paginate(array, page_size, page_number) {
    // human-readable page numbers start with 1
    return array.slice((page_number - 1) * page_size, page_number * page_size);
}

app.listen(port)
