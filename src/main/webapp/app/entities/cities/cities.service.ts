import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Cities } from './cities.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CitiesService {

    private resourceUrl = 'api/cities';

    constructor(private http: Http) { }

    create(cities: Cities): Observable<Cities> {
        const copy = this.convert(cities);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(cities: Cities): Observable<Cities> {
        const copy = this.convert(cities);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Cities> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(cities: Cities): Cities {
        const copy: Cities = Object.assign({}, cities);
        return copy;
    }
}
