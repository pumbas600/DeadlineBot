import React, {Component, useState} from "react";
import {IconButton, ListItem, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import {SpacedRow} from "./StyledComponents";
import Course from "../models/Course";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import Updatable from "./Updatable";

interface Props {
    course: Course;
    removeCourse: (course: string) => void;
}

interface State {
    isPublic: boolean;
}

class TrackedCourse extends React.Component<Props, State> implements Updatable<Course> {

    constructor(props: Props) {
        super(props);
        this.state = {
            isPublic: this.props.course.is_public
        };
    }

    hasBeenUpdated(): boolean {
        return this.state.isPublic === this.props.course.is_public;
    }
    getUpdate(): Course {
        return { ...this.props.course, is_public: this.state.isPublic };
    }

    render() {
        return (
            <ListItem disablePadding>
                <SpacedRow>
                    <Typography variant="subtitle1">
                        {this.props.course.name}
                    </Typography>
                    <SpacedRow>
                        <IconButton onClick={e => this.props.removeCourse(this.props.course.name)}>
                            <CloseIcon/>
                        </IconButton>
                        <IconButton onClick={e => this.setState({ isPublic: !this.state.isPublic })}>
                            {this.state.isPublic ? (
                                <VisibilityIcon/>
                            ) : (
                                <VisibilityOffIcon/>
                            )}
                        </IconButton>
                    </SpacedRow>
                </SpacedRow>

            </ListItem>
        );
    }
}

export default TrackedCourse;