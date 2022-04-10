import User from "./models/User";
import axios, {AxiosError} from "axios";

const OK = 200;

function handleError(err: any) {
    const error = err as AxiosError;
    if (error.response) {
        console.error(error.response.data);
        console.error(error.response.status);
        console.error(error.response.headers);
    } else if (error.request) {
        console.error(error.request);
    } else {
        console.error("Error", error.message);
    }
}

async function fetchUser(discordId: string): Promise<User | null> {
    try {
        const { data: user } = await axios.get<User>(`/users/${discordId}`);
        return user;
    } catch (error) {
        handleError(error);
    }
    return null;
}


export { fetchUser }