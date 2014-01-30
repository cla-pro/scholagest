function monitorDomForLazyLoading(dom, loadData) {
	dom.areDataLoading = false;
	div.onscroll = function(e) {
		if (isBottomReached(dom) && !dom.areDataLoading) {
			div.areDataLoading = true;
			loadData();
		}
	};
};
function lazyLoadingDomDataLoaded(dom) {
	dom.areDataLoading = false;
};
function isBottomReached(dom) {
	return dom.scrollTop >= (ul.offsetHeight - dom.offsetHeight);
};