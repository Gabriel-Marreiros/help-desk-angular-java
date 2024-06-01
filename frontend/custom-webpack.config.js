const webpack = require('webpack');
/*
  Este arquivo javascript é necessário para configurar as variáveis de ambiente e podermos sobscrever as variáveis de ambiente via docker
*/
module.exports = {
  plugins: [
    new webpack.DefinePlugin({
      $ENV: {
        API_URL: JSON.stringify(process.env.API_URL)
      }
    })
  ]
};
