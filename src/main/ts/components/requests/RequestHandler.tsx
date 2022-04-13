import React, {useEffect, useState} from "react";
import axios from "axios";

enum RequestState {
    Loading,
    Success,
    Error
}

interface Props<T> {
    url: string;
    loading?: JSX.Element;
    success: (data: T) => JSX.Element;
    error?: JSX.Element;
    callback?: (data: T) => void;
}

interface State<T> {
    requestState: RequestState;
    data: T | null;
    error?: string;
}

const RequestHandler = <T extends object>(props: Props<T>): JSX.Element => {

    const [state, setState] = useState<State<T>>({ requestState: RequestState.Loading, data: null });

    useEffect(() => {
        async function doRequest() {
            try {
                const { data } = await axios.get<T>(props.url);
                setState({ requestState: RequestState.Success, data: data });
            } catch (error) {
                setState({ requestState: RequestState.Error, data: null, error: (error as Error).message});
            }
        }

        doRequest();
    }, []);

    switch(state.requestState) {
        case RequestState.Loading:
            return props.loading ?? <p>Data is loading :)</p>;
        case RequestState.Success:
            if (state.data !== null) // Sanity check. This should always be true.
                return props.success(state.data);

            setState({ ...state,
                requestState: RequestState.Error,
                error: `Somehow RequestState is ${state.requestState} but data is null`
            });
            break;
        case RequestState.Error:
            console.error(state.error ?? 'There was no error message')
            return props.error ?? <p>Something went wrong...</p>;
    }

    return <p/>; // Just because typescript can't infer that there is no path to this...
}

export default RequestHandler;
export { RequestState }