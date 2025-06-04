let websocket;
const channelToSubscription = new Map();

function getNextBarTime(barTime, resolution) {
	let oneBarTime = 0;
	if (resolution === "1")
		oneBarTime = 60 * 1000;
	else if (resolution === "5")
		oneBarTime = 5 * 60 * 1000;
	else if (resolution === "15")
		oneBarTime = 15 * 60 * 1000;
	else if (resolution === "30")
		oneBarTime = 30 * 60 * 1000;
	else if (resolution === "60")
		oneBarTime = 60 * 60 * 1000;	
	else if (resolution === "240")
		oneBarTime = 4 * 60 * 60 * 1000;	
	else if (resolution === "1D")
		oneBarTime = 24 * 60 * 60 * 1000;
		
	const date = new Date(barTime + oneBarTime);
	console.log("nextbarTime: " + oneBarTime + " " + resolution);
	return date.getTime();
}

export function connect(path, access_token)
{
	console.log("websocketPath: " + path)
	
	websocket = new WebSocket(path, ['Bearer', access_token] );
	console.log("connect: " + path);
	
	websocket.onopen = function(){
		console.log("open");
	}

	websocket.onclose = function(){
		const response = { type: "disconnect" }
		window.ReactNativeWebView.postMessage(JSON.stringify(response));
	}

	websocket.onmessage = (e) => {
		var message = JSON.parse(e.data);
		//console.log(message);
		if (message.actionAck !== "tick_ack")
			return;
		
		const channelString = message.data.Symbol;
		const subscriptionItem = channelToSubscription.get(channelString);
		if (subscriptionItem === undefined) {
			return;
		}

		subscriptionItem.handlers.forEach(handler => 
		{
			const lastDailyBar = handler.lastDailyBar;
			const nextBarTime = getNextBarTime(lastDailyBar.time, handler.resolution);
			
			//console.log("count: " + subscriptionItem.resolution);
			//console.log("message data time: " + message.data.Time);
			//console.log("nextDailyBarTime: " + nextBarTime / 1000);
			//console.log("nextBarTime: " + nextBarTime / 1000);
	
			let bar;
			if (message.data.Time >= nextBarTime / 1000) {
				bar = {
					time: nextBarTime,
					open: message.data.Bid,
					high: message.data.Bid,
					low: message.data.Bid,
					close: message.data.Bid,
				};
				console.log('[socket] Generate new bar', bar);
			} else {
				bar = {
					...lastDailyBar,
					high: Math.max(lastDailyBar.high, message.data.Bid),
					low: Math.min(lastDailyBar.low, message.data.Bid),
					close: message.data.Bid,
				};
				console.log('[socket] Update the latest bar by price', message.data.Bid);
			}
			handler.lastDailyBar = {...bar};

			// send data to every subscriber of that symbol
			handler.callback(bar)
			
		});
	}
}

export function subscribeOnStream(
	symbolInfo,
	resolution,
	onRealtimeCallback,
	subscriberUID,
	onResetCacheNeededCallback,
	lastDailyBar,
) {
	const channelString = symbolInfo.name;
	const handler = {
		id: subscriberUID,
		resolution: resolution,
		lastDailyBar: lastDailyBar,
		callback: onRealtimeCallback,
	};
	let subscriptionItem = channelToSubscription.get(channelString);
	if (subscriptionItem) {
		// already subscribed to the channel, use the existing subscription
		subscriptionItem.handlers.push(handler);
		return;
	}
	subscriptionItem = {
		handlers: [handler],
	};
	channelToSubscription.set(channelString, subscriptionItem);
	console.log('[subscribeBars]: Subscribe to streaming. Channel:', channelString);
	
	var subscribeObject = {
		pid: subscriberUID,
		action: "subscribe",
		data: {
			Symbol: symbolInfo.name
		}
	}
	
	websocket.send(JSON.stringify(subscribeObject));
	console.log("send");
}

export function unsubscribeFromStream(subscriberUID) {
	// find a subscription with id === subscriberUID
	console.log("unsubscribeFromStream: " + subscriberUID);
	for (const channelString of channelToSubscription.keys()) {
		const subscriptionItem = channelToSubscription.get(channelString);
		const handlerIndex = subscriptionItem.handlers.findIndex(handler => handler.id === subscriberUID);

		if (handlerIndex !== -1) {
			// remove from handlers
			subscriptionItem.handlers.splice(handlerIndex, 1);

			if (subscriptionItem.handlers.length === 0) {
				// unsubscribe from the channel, if it was the last handler
				console.log('[unsubscribeBars]: Unsubscribe from streaming. Channel:', channelString);
				channelToSubscription.delete(channelString);
				break;
			}
		}
	}
}
