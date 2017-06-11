import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { NearepTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CitiesDetailComponent } from '../../../../../../main/webapp/app/entities/cities/cities-detail.component';
import { CitiesService } from '../../../../../../main/webapp/app/entities/cities/cities.service';
import { Cities } from '../../../../../../main/webapp/app/entities/cities/cities.model';

describe('Component Tests', () => {

    describe('Cities Management Detail Component', () => {
        let comp: CitiesDetailComponent;
        let fixture: ComponentFixture<CitiesDetailComponent>;
        let service: CitiesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NearepTestModule],
                declarations: [CitiesDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CitiesService,
                    EventManager
                ]
            }).overrideTemplate(CitiesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CitiesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CitiesService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cities(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cities).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
