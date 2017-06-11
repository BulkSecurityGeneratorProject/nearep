import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PostAddress } from './post-address.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PostAddressService {

    private resourceUrl = 'api/post-addresses';

    constructor(private http: Http) { }

    create(postAddress: PostAddress): Observable<PostAddress> {
        const copy = this.convert(postAddress);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(postAddress: PostAddress): Observable<PostAddress> {
        const copy = this.convert(postAddress);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PostAddress> {
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

    private convert(postAddress: PostAddress): PostAddress {
        const copy: PostAddress = Object.assign({}, postAddress);
        return copy;
    }
}
