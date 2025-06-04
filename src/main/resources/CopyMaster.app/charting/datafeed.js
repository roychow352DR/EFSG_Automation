import {
	makeApiRequest
} from './helpers.js?v1.1';
import {
	connect,
	subscribeOnStream,
	unsubscribeFromStream,
} from './streaming.js?v1.1';

const lastBarsCache = new Map();

const configurationData = {
	supported_resolutions: ['1', '5', '15', '30', '1h', '4h', '24h', '1D', '1W']
};

let requestPath = "";
let requestToken = "";
let priceScale = 1000;

export function setPath(path, wsPath, access_token){
	requestPath = path;
	requestToken = access_token;
	let websocketPath = wsPath;
	connect(websocketPath, access_token);
}

export function setScale(scale)
{
    priceScale = scale;
}

export default {
	onReady: (callback) => {
		console.log('[onReady]: Method call');
		setTimeout(() => callback(configurationData));
	},


	resolveSymbol: async (
		symbolName,
		onSymbolResolvedCallback,
		onResolveErrorCallback,
	) => {
		console.log('[resolveSymbol]: Method call', symbolName);
		const symbolInfo = {
			ticker: symbolName,
			name: symbolName,
			description: symbolName,
			timezone: "America/Chicago",
			pricescale: priceScale,
			minmov: 1,
			has_intraday: true,
			has_no_volume: true,
			has_weekly_and_monthly: false,
            has_daily: true,
            daily_multipliers: ['1'],
            intraday_multipliers: ['1', '5', '15', '30', '60'],
            session: "1600-1559:2345671",
			supported_resolutions: configurationData.supported_resolutions,
			volume_precision: 2,
			data_status: 'streaming',
		};

		console.log('[resolveSymbol]: Symbol resolved', symbolName);
		onSymbolResolvedCallback(symbolInfo);
	},

	getBars: async (symbolInfo, resolution, periodParams, onHistoryCallback, onErrorCallback) => {
		const { from, to, firstDataRequest } = periodParams;
		console.log('[getBars]: Method call', symbolInfo.name, resolution, from, to, firstDataRequest);
		
		const urlParameters = 
		{
			symbol: symbolInfo.name,
			start: from,
			end: to,
			resolution: resolution
		};
		
		const requestOptions = {
			method: 'GET',
			headers: {
			  'Authorization': 'Bearer ' + requestToken
			}
		};
		
		const query = Object.keys(urlParameters)
			.map(name => `${name}=${encodeURIComponent(urlParameters[name])}`)
			.join('&');
		
		try {
			const response = await makeApiRequest(`${requestPath}historicalBars?${query}`, requestOptions);
			
			//queryBars(symbolInfo, resolution, from, to , onHistoryCallback);
			
			if (response && response.status === false || response.data.length === 0) {
				// "noData" should be set if there is no data in the requested period.
				onHistoryCallback([], {
					noData: true,
				});
				return;
			}
			let bars = [];
			response.data.forEach(bar => {
				if (bar.Time >= from && bar.Time < to) {
					bars = [...bars, {
						time: bar.Time * 1000,
						low: bar.Low,
						high: bar.High,
						open: bar.Open,
						close: bar.Close,
					}];
				}
			});
			if (firstDataRequest) {
				lastBarsCache.set(symbolInfo.full_name, {
					...bars[bars.length - 1],
				});
			}
			console.log(`[getBars]: returned ${bars.length} bar(s)`);
			onHistoryCallback(bars, {
				noData: false,
			});
		} catch (error) {
			console.log('[getBars]: Get error', error);
			onErrorCallback(error);
		}
	},

	subscribeBars: (
		symbolInfo,
		resolution,
		onRealtimeCallback,
		subscribeUID,
		onResetCacheNeededCallback,
	) => {
		console.log('[subscribeBars]: Method call with subscribeUID:', subscribeUID);
		subscribeOnStream(
			symbolInfo,
			resolution,
			onRealtimeCallback,
			subscribeUID,
			onResetCacheNeededCallback,
			lastBarsCache.get(symbolInfo.full_name),
		);
	},

	unsubscribeBars: (subscriberUID) => {
		console.log('[unsubscribeBars]: Method call with subscriberUID:', subscriberUID);
		unsubscribeFromStream(subscriberUID);
	},
};
