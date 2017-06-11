import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { NearepTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DistrictsDetailComponent } from '../../../../../../main/webapp/app/entities/districts/districts-detail.component';
import { DistrictsService } from '../../../../../../main/webapp/app/entities/districts/districts.service';
import { Districts } from '../../../../../../main/webapp/app/entities/districts/districts.model';

describe('Component Tests', () => {

    describe('Districts Management Detail Component', () => {
        let comp: DistrictsDetailComponent;
        let fixture: ComponentFixture<DistrictsDetailComponent>;
        let service: DistrictsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NearepTestModule],
                declarations: [DistrictsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DistrictsService,
                    EventManager
                ]
            }).overrideTemplate(DistrictsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DistrictsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistrictsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Districts(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.districts).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
