import React from "react";
import {Typography} from "@mui/material";
import {SpacedRow} from "./StyledComponents";

interface Props {
    label: string;
}

const LabelledControl: React.FC<Props> = (props) => {
    return (
        <SpacedRow>
            <Typography variant="subtitle1">
                {props.label}
            </Typography>
            {props.children}
        </SpacedRow>
    );
}

export default LabelledControl;