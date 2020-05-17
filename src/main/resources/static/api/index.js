const METHOD = {
    PUT(data) {
        return {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        };
    },
    DELETE() {
        return {
            method: "DELETE"
        };
    },
    POST(data) {
        return {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        };
    }
};

const api = (() => {
    const request = (uri, config) => fetch(uri, config).then(async data => {
        if (!data.ok) {
            let error = null;
            await data.json().then(message => {
                error = new Error(message.errorMessage);
            });
            throw error;
        } else {
            return data.json();
        }
    }).catch(e => alert(e.message));

    const station = {
        get() {
            return request(`/stations`);
        },
        create(data) {
            return request(`/stations`, METHOD.POST(data));
        },
        update(data, id) {
            return request(`/stations/${id}`, METHOD.PUT(data));
        },
        delete(id) {
            return fetch(`/stations/${id}`, METHOD.DELETE());
        }
    };

    const line = {
        get(id = "") {
            return request(`/lines/${id}`);
        },
        create(data) {
            return request(`/lines`, METHOD.POST(data));
        },
        update(data, id) {
            return request(`/lines/${id}`, METHOD.PUT(data));
        },
        delete(id) {
            return fetch(`/lines/${id}`, METHOD.DELETE());
        },
        getWithStations() {
            return request(`/lines/stations`);
        },
        createLineStation(data, id) {
            return fetch(`/lines/${id}/stations`, METHOD.POST(data));
        },
        deleteLineStation(id, stationId) {
            return fetch(`/lines/${id}/stations/${stationId}`, METHOD.DELETE());
        }
    };

    return {
        station,
        line
    };
})();

export default api;