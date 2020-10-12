module.exports = {
  configureWebpack: {
    devtool: 'source-map'
  },
  "transpileDependencies": [
    "vuetify"
  ],
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
    },
    port: 4545
  }
}