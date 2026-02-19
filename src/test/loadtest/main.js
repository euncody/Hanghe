// k6 v0.48+
import { sleep } from 'k6';
import http from 'k6/http';
import { check } from 'k6';

const BASE = __ENV.BASE_URL || 'http://localhost:8089';
const USER = __ENV.ACCESS_USER || 'test1';
const PASS = __ENV.ACCESS_PASS || 'test1';

export const options = {
    scenarios: {
        smoke: { executor: 'constant-vus', vus: 1, duration: '1m' },
        load:  { executor: 'ramping-arrival-rate',
            startRate: 1, timeUnit: '1s',
            preAllocatedVUs: 50, maxVUs: 500,
            stages: [
                { target: 30, duration: '5m' },
                { target: 30, duration: '10m' },
            ]},
        spike: { executor: 'ramping-arrival-rate',
            startRate: 1, timeUnit: '1s',
            preAllocatedVUs: 100, maxVUs: 800,
            stages: [
                { target: 100, duration: '30s' },
                { target: 10,  duration: '2m' },
            ]},
        soak:  { executor: 'constant-arrival-rate', rate: 10, timeUnit: '1s',
            preAllocatedVUs: 50, maxVUs: 300, duration: '30m' },
    },
    thresholds: {
        http_req_failed: ['rate<0.005'],
        http_req_duration: ['p(95)<800','p(99)<1200'],
        'http_req_duration{scenario:spike}': ['p(95)<1500'],
    },
};

export default function () {
    const list = http.get(`${BASE}/api/products`, auth);
    check(list, { 'list 200': (r) => r.status === 200 });


    sleep(1);
}



