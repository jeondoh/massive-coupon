let authData = [];
const authFile = open("auth.json");
const trimContent = authFile.trim();
authData = JSON.parse(trimContent);

let token = null;

export function getToken(vuIndex) {
    if (authData && authData.length > 0) {
        return authData[vuIndex % authData.length];
    } else {
        return null;
    }
}
