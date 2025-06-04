// Make requests to Chart
export async function makeApiRequest(url, requestOptions) {
	try {
		const response = await fetch(url, requestOptions);
		return response.json();
	} catch (error) {
		throw new Error(`Chart Historical request error: ${error.status}`);
	}
}