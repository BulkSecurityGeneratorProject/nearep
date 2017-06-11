import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { NearepTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProvincesDetailComponent } from '../../../../../../main/webapp/app/entities/provinces/provinces-detail.component';
import { ProvincesService } from '../../../../../../main/webapp/app/entities/provinces/provinces.service';
import { Provinces } from '../../../../../../main/webapp/app/entities/provinces/provinces.model';

describe('Component Tests', () => {

    describe('Provinces Management Detail Component', () => {
        let comp: ProvincesDetailComponent;
        let fixture: ComponentFixture<ProvincesDetailComponent>;
        let service: ProvincesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NearepTestModule],
                declarations: [ProvincesDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProvincesService,
                    EventManager
                ]
            }).overrideTemplate(ProvincesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProvincesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProvincesService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Provinces(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.provinces).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
