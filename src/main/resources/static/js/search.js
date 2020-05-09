const searchSubmit = () => {
    let inputSearch = $("#searchString").val();
    let form = $("#searchForm");
    let btn = $("#searchButton");
    let url = window.location.pathname +"?search=" + inputSearch;
    btn.attr("href", url);
}