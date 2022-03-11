export const createSocket = (config, params) => {
  const socket = new WebSocket(config.url)
  socket.onerror = event => {
    typeof config.onError === 'function' && config.onError(event, socket)
  }
  socket.onclose = event => {
    typeof config.onClose === 'function' && config.onClose(event, socket)
  }
  socket.onopen = event => {
    socket.send(typeof params === 'string' ? params : JSON.stringify(params))
    typeof config.onOpen === 'function' && config.onOpen(event, socket)
  }
  socket.onmessage = message => {
    typeof config.onMessage === 'function' && config.onMessage(message, socket)
  }
}
