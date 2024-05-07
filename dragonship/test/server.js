import status from "@http-util/status-i18n";

import express from "express";

import db from "./db.json" with {type: "json"};

const app = express();
const port = process.env.PORT || 1337;

const getDurationInMilliseconds = (start) => {
    const NS_PER_SEC = 1e9
    const NS_TO_MS = 1e6
    const diff = process.hrtime(start)

    return (diff[0] * NS_PER_SEC + diff[1]) / NS_TO_MS
}

const logger = function (req, res, next) {
    const start = process.hrtime()
    console.log(`--> ${req.method} ${req.url}`);


    res.on('finish', () => {
        const durationInMilliseconds = getDurationInMilliseconds(start)
        console.log(`<-- ${res.statusCode} ${status[res.statusCode].toUpperCase()} (${durationInMilliseconds.toLocaleString()} ms)`);
    })

    next();
}

app.use(logger);
app.use(express.json());

app.get('/my/ships', function (req, res) {
    const ships = db['ships']

    paginate(() => ships, req, res)
})

function paginate(dataSupplier, req, res) {
    function paginate(array, page_number, page_size) {
        // human-readable page numbers start with 1
        return array.slice((page_number - 1) * page_size, page_number * page_size);
    }

    const params = req.query || {}

    const page = params?.page || 1
    const limit = params?.limit || 10

    const data = paginate(dataSupplier(), page, limit)
    res.send({meta: {total: data.length, page, limit}, data})
}

console.log(`Running on '${port}'`)
app.listen(port)
