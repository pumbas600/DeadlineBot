import React, {useState} from "react";
import {SpacedRow} from "./StyledComponents";
import {IconButton, TextField} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';

interface Props {
    onAdd: (course: string) => void;
}

const AddTrackedCourse: React.FC<Props> = (props) => {

    const [courseName, setCourseName] = useState<string>('');

    function handleInput(e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        setCourseName(e.target.value);
    }

    function handleSubmit() {
        if (courseName.length !== 0) {
            props.onAdd(courseName);
            setCourseName('');
        }
    }

    return (
        <SpacedRow sx={{ marginTop: 0.5 }}>
            <TextField
                onChange={handleInput}
                size="small"
                label="Course Name"
                variant="outlined"
                value={courseName}
            />
            <IconButton
                disabled={courseName.length === 0}
                onClick={e => handleSubmit()}
            >
                <AddIcon />
            </IconButton>
        </SpacedRow>
    );
}

export default AddTrackedCourse;