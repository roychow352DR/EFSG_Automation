// Datafeed implementation, will be added later
import Datafeed from './datafeed.js';

let urlParams = new URLSearchParams(location.search);
let orientation = "PORTRAIT";
let symbol = "GOLD";
let interval = "5";
let theme = 'Light';
if (urlParams.has('orientation'))
{
	orientation = urlParams.get('orientation')
}
if (urlParams.has('symbol'))
{
	symbol = urlParams.get('symbol')
}
if (urlParams.has('interval'))
{
	interval = urlParams.get('interval')
}
if (urlParams.has('theme'))
{
	theme = urlParams.get('theme')
}


window.tvWidget = new TradingView.widget({
	auto_save_delay: 3,
	symbol: symbol, // default symbol
	interval: interval, // default interval
	timezone: "Europe/Moscow",
	fullscreen: true, // displays the chart in the fullscreen mode
	container: 'tv_chart_container',
	datafeed: Datafeed,
	library_path: 'charting_library/',
	disabled_features: orientation.toUpperCase() === "PORTRAIT" ? ["header_resolutions", "header_symbol_search", "header_undo_redo", "header_compare", "left_toolbar", "timeframes_toolbar", "display_market_status", "control_bar", "legend_widget", "items_favoriting", "header_screenshot", "header_fullscreen_button"] : ["header_resolutions", "header_symbol_search", "header_undo_redo", "header_compare", "timeframes_toolbar", "display_market_status", "control_bar", "legend_widget", "items_favoriting", "header_screenshot", "header_fullscreen_button"],
	enabled_features: ["move_logo_to_main_pane"],
	header_widget_buttons_mode: 'fullsize',
	theme: theme
});

