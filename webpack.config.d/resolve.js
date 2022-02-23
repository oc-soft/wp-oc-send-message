
/**
 * setup webpack resolve
 */
class Resolve {

  /**
   * setup configuration
   */
  setup(config) {

    const path = require('path')
    config.resolve = config.resolve || {}
    const resolve = config.resolve
    resolve.alias = resolve.alias || {}
    resolve.alias['dialog-polyfill'] = 'dialog-polyfill/dist/dialog-polyfill.js'
  }
}


((config)=>{
  const resolve = new Resolve()
  resolve.setup(config)
})(config)
// vi: se ts=2 sw=2 et:
