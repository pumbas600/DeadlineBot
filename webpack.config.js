const path = require('path');

module.exports = {
    devtool: 'source-map',
    mode: "development",
    watch: true,
    output: {
        path: path.resolve(process.cwd(), 'src/main/resources/static/dist'),
        filename: 'react-app.js'
    },
    entry: {
        app: path.resolve(process.cwd(), 'src/main/ts/index.tsx')
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: [
                    { loader: 'ts-loader', options: { happyPackMode: true } }
                ],
                exclude: path.resolve(process.cwd(), 'node_modules'),
                include: path.resolve(process.cwd(), 'src'),
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
                exclude: path.resolve(process.cwd(), 'node_modules'),
                include: path.resolve(process.cwd(), 'src'),
            },
        ],
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.jsx'],
    },
}