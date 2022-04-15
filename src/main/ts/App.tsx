import React from 'react';
import './App.css';
import { configureAxios } from "./config/AppConfig";
import User from "./models/User";
import CalendarController from "./components/CalendarController";
import RequestHandler from "./components/requests/RequestHandler";

configureAxios();

//TODO: Basic state

function App() {

    const discordId = '260930648330469387';

    return (
        <div className="App">
            <RequestHandler<User>
                url={`/users/${discordId}`}
                success={(user) => <CalendarController user={user}/>}
            />
        </div>
    );
}

export default App;
