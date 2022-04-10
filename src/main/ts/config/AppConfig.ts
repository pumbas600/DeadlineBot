import axios from "axios";

const API_PREFIX = '/api/v1';

function configureAxios() {
    axios.defaults.baseURL = API_PREFIX;
}

export { configureAxios, API_PREFIX };