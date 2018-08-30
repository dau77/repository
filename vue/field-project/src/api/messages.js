const messages = {
    message(resp, defaultMsg) {
        let success = resp.status >= 200 && resp.status < 300,
            type = success ? 'success' : 'failure',
            msgSize = resp.data && resp.data.messages && resp.data.messages.length,
            msg = msgSize ? resp.data.messages[msgSize - 1].msg : defaultMsg;
        msg = msg || (success ? '操作成功' : '操作失败');
        return { type: type, message: msg };
    }
}

export default messages