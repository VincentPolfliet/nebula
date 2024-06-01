import {Plugin} from 'vite'
import {connect} from "node:net";
import colors from 'picocolors'

export class Config {
    port: number = 9000
    log: boolean = false
    exitOnError: boolean = true
}

export const socketReload = (config: Config = new Config()): Plugin => ({
    name: 'vite-plugin-socket-reload',

    configureServer(server) {
        const logger = server.config.logger

        const socket = connect(config.port, "localhost", () => {
            logger.info(`Connected to socket server on ${colors.green(config.port)}`);
        })

        socket.on('error', async error => {
            if (config.log) {
                logger.error(error.stack);
            }

            if (config.exitOnError) {
                logger.info("Closing server...")
                await server.close();
            }
        });

        socket.on('end', async () => {
            logger.info("Closing server...")
            await server.close();
        })
    }
});

export default socketReload
