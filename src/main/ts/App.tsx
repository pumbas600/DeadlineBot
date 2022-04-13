import React, {useEffect, useState} from 'react';
import './App.css';
import {Box, Fab, List, ListItem, Tooltip} from "@mui/material";
import UserData from "./models/UserData";
import axios from "axios";
import { configureAxios } from "./config/AppConfig";
import User from "./models/User";
import Calendar from "./components/Calendar";
import CalendarController from "./components/CalendarController";
import RequestHandler from "./components/requests/RequestHandler";

configureAxios();

//TODO: Basic state

function App() {

    const discordId = '260930648330469387';
    const [userData, setUserData] = useState<UserData>({ linkedDiscordId: '-1', trackedCalendars: [] });
    const [user, setUser] = useState<User>({ courses: [] });

    useEffect(() => {
        async function fetchUserData() {
            try {
                const { data: user } = await axios.get<User>(`/users/${discordId}`);
                setUser(user);
            } catch (error) {
                console.error(error);
            }
        }

        fetchUserData();
    }, []);

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
