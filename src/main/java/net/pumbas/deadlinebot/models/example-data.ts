const ObjectID = (_id: string): string => {
    return _id;
}

const user = {
    _id: ObjectID("DISCORD_ID"),
    calendar_id: "Some id",
    courses: [                // Set - No double ups should be allowed
        ObjectID("AAAA"), // References course document
        ObjectID("AAAB"),
        ObjectID("AABC"),
    ]
}

const course = {
    _id: ObjectID("AAAA"),
    owner_id: ObjectID("DISCORD_ID"), // References user document
    is_public: true, // Must be public for people who aren't the owner to track
    name: "SOFTENG 281"
}

export default ObjectID;