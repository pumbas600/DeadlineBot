export default interface Course {
    id: string | undefined;
    owner_id: string | undefined;
    name: string;
    is_public: boolean;
}