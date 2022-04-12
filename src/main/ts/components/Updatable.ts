export default interface Updatable<T = {}> {
    hasBeenUpdated(): boolean;
    getUpdate(): T;
}