import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { NearepTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CountriesDetailComponent } from '../../../../../../main/webapp/app/entities/countries/countries-detail.component';
import { CountriesService } from '../../../../../../main/webapp/app/entities/countries/countries.service';
import { Countries } from '../../../../../../main/webapp/app/entities/countries/countries.model';

describe('Component Tests', () => {

    describe('Countries Management Detail Component', () => {
        let comp: CountriesDetailComponent;
        let fixture: ComponentFixture<CountriesDetailComponent>;
        let service: CountriesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NearepTestModule],
                declarations: [CountriesDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CountriesService,
                    EventManager
                ]
            }).overrideTemplate(CountriesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CountriesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CountriesService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Countries(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.countries).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
