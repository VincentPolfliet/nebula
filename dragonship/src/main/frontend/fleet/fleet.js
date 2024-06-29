import '../main.js'
import './fleet.scss'

const ws = new WebSocket("ws://localhost:8080/ws/ship");

ws.addEventListener("message", (event) => {
    const updatedShipData = JSON.parse(event.data);
    console.log("ship: " + updatedShipData["ship"])
});
